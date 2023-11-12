package com.avsb.pdv.service;

import com.avsb.pdv.dto.ProductDTO;
import com.avsb.pdv.dto.ProductInfoDTO;
import com.avsb.pdv.dto.SaleDTO;
import com.avsb.pdv.dto.SaleInfoDTO;
import com.avsb.pdv.entity.ItemSale;
import com.avsb.pdv.entity.Product;
import com.avsb.pdv.entity.Sale;
import com.avsb.pdv.entity.User;
import com.avsb.pdv.exceptions.InvalidOperationException;
import com.avsb.pdv.exceptions.NoItemException;
import com.avsb.pdv.repository.ItemSaleRepository;
import com.avsb.pdv.repository.ProductRepository;
import com.avsb.pdv.repository.SaleRepository;
import com.avsb.pdv.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ItemSaleRepository itemSaleRepository;

    public List<SaleInfoDTO> findAll(){
        return saleRepository.findAll().stream().map(sale -> getSaleInfo(sale)).collect(Collectors.toList());
    }

    private SaleInfoDTO getSaleInfo(Sale sale) {
        SaleInfoDTO saleInfoDTO = new SaleInfoDTO();
        saleInfoDTO.setUser(sale.getUser().getName());
        saleInfoDTO.setDate(sale.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        saleInfoDTO.setProducts(getProductInfo(sale.getItems()));

        return saleInfoDTO;
    }

    private List<ProductInfoDTO> getProductInfo(List<ItemSale> items) {
        return items.stream().map(item -> {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO();
            productInfoDTO.setDescription(item.getProduct().getDescription());
            productInfoDTO.setQuantity(item.getQuantity());

            return  productInfoDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public long save(SaleDTO sale){
        User user = userRepository.findById(sale.getUserid())
                .orElseThrow(() -> new NoItemException("Usuário não encontrado"));

        Sale newSale = new Sale();
        newSale.setUser(user);
        newSale.setDate(LocalDate.now());
        List<ItemSale> items = getItemSale(sale.getItems());

        newSale = saleRepository.save(newSale);

        saveItemSale(items, newSale);

        return newSale.getId();
    }

    private void saveItemSale(List<ItemSale> items, Sale newSale) {
        for (ItemSale item: items){
            item.setSale(newSale);
            itemSaleRepository.save(item);
        }
    }

    private List<ItemSale> getItemSale(List<ProductDTO> products){

        if(products.isEmpty()){
            throw new InvalidOperationException("Não é possível adicionar a venda sem itens.");
        }

        return products.stream().map(item ->{
            Product product = productRepository.getReferenceById(item.getProductid());

            ItemSale itemSale = new ItemSale();
            itemSale.setProduct(product);
            itemSale.setQuantity(item.getQuantity());

            if(product.getQuantity() == 0){
                throw new NoItemException("Produto sem estoque!");
            } else if (product.getQuantity() < item.getQuantity()) {
                throw new InvalidOperationException(
                        String.format("A quantidade de itens da venda (%s) é maior do que a quantidade disponível no estoque (%s)",item.getQuantity(), product.getQuantity()));
            }

            int total = product.getQuantity() - item.getQuantity();
            product.setQuantity(total);
            productRepository.save(product);

            return itemSale;
        }).collect(Collectors.toList());
    }

    public SaleInfoDTO getById(long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NoItemException("Venda não encontrada!"));
        return getSaleInfo(sale);
    }
}