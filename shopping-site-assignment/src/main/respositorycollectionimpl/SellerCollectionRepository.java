package main.respositorycollectionimpl;

import main.entities.Seller;
import main.enums.Roles;
import main.repository.interfaces.SellerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SellerCollectionRepository implements SellerRepository {
   List<Seller> sellers = new ArrayList<>();
    {
        sellers.add(new Seller(1L, "Crompton", Roles.SELLER, LocalDateTime.now()));
    }
    @Override
    public List<Seller> fetchSellers() {
        return sellers;
    }

    @Override
    public Optional<Seller> fetchById(Long id) {
        return sellers.stream().filter(s->s.getId().equals(id)).findAny();
    }
}
