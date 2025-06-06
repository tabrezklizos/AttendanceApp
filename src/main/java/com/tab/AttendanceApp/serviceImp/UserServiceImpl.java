package com.tab.AttendanceApp.serviceImp;

import com.tab.AttendanceApp.entity.User;
import com.tab.AttendanceApp.enumeration.UserRole;
import com.tab.AttendanceApp.exception.UserExistException;
import com.tab.AttendanceApp.repository.UserRepository;
import com.tab.AttendanceApp.request.UserRequest;
import com.tab.AttendanceApp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;



    @Override
    public List<User> getAllUser() {
        List<User> allUser = userRepository.findAll();
        return allUser;

    }

    @Override
    public Boolean saveUser(UserRequest request) throws UserExistException {

        boolean userExisted = userRepository.existsUserByEmail(request.getEmail());

        if(userExisted){
            throw new UserExistException("user with email "+request.getEmail()+" is existed");
        }

        UserRole role = UserRole.valueOf(request.getUserRole().toUpperCase());

        User user = mapper.map(request, User.class);
                user.setIsDeleted(false);
                // user.setCreatedBy(1L);
               // user.setCreatedDate(new Date());

        User savedUser = userRepository.save(user);

        if(ObjectUtils.isEmpty(savedUser)){
            return false;
        }
        return true;
    }
}


















