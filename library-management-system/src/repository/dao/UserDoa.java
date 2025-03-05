package repository.dao;

import customexception.DataInsertException;
import model.entity.user.User;

import java.util.Objects;

public class UserDoa extends DataBaseImpl{
    @Override
    public boolean add(Object object) throws DataInsertException {
        if(!(object instanceof User user)){
            return false;
        }
        return  Objects.nonNull(usersDb.put(user.getEmail(),user));
    }

    @Override
    public void read() {
        try {
            usersDb.forEach((email, user) -> System.out.println(user));
        }catch (RuntimeException e){
            throw new RuntimeException("some thing happened during the reading of data");
        }
    }

    //BOTH SAME ADD AND UPDATE
    @Override
    public boolean update(Object object) {
        try{
            if(!(object instanceof User user)){
                return false;
            }
            return Objects.nonNull(usersDb.put(user.getEmail(),user));
        }catch (Exception e){
            throw new RuntimeException("something happened during updating the data");
        }
    }

    @Override
    public boolean delete(Object o) {
        return false;
    }
}
