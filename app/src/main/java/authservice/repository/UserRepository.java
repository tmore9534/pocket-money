package authservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import authservice.entities.UserInfo;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {
    public UserInfo findByUsername(String username);
}
