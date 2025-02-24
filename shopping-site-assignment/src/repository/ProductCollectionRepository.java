package repository;


import entities.Product;
import entities.Seller;
import enums.Currency;
import enums.ProductType;
import repository.interfaces.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

public  class ProductCollectionRepository implements ProductRepository {

    private  final List<Product> products;

    public ProductCollectionRepository() {
        this.products = new ArrayList<>();
        String []names = {"IPHONE", "Sofa", "MIXER", "T-shirt", "Maskara"};
        String []seller = {"Apple", "Sleepwell", "Bajaj", "Cambridge", "Loreal"};
        Random rand = new Random();
        ProductType[] types = ProductType.values();
        for (int i = 0; i < types.length; i++) {
            populate(names[i], Currency.INR, rand.nextInt(0, 9000), types[i], seller[i]);
        }
    }
    private  void populate(String name, Currency currency, double price, ProductType type, String sellerName){
        products.add(new Product(name, currency, price, type, new Seller(sellerName)));
    }
    public    List<Product> fetchProducts() {

        return products;
    }
    public    Optional<Product> fetchProductById(Long id) {
        Map<Long, Product> map = products.stream().collect(Collectors.toConcurrentMap(Product::getId, p -> p));
        return map.containsKey(id) ? Optional.of(map.get(id)) : Optional.empty();
    }
    public  Optional<Product> fetchProductByName(String name)
    {
        return products.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst();
    }

}