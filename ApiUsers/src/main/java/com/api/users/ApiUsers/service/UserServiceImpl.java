package com.api.users.ApiUsers.service;

import com.api.users.ApiUsers.entity.AlbumsServiceClient;
import com.api.users.ApiUsers.entity.UserEntity;
import com.api.users.ApiUsers.model.AlbumResponseModel;
import com.api.users.ApiUsers.repository.UserRepository;
import com.api.users.ApiUsers.shared.UserDto;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService{
    Logger logger= LoggerFactory.getLogger(this.getClass());

    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;
    //RestTemplate restTemplate;
    Environment env;
    AlbumsServiceClient albumsServiceClient;

    @Autowired
    public UserServiceImpl(Environment env,AlbumsServiceClient albumsServiceClient, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        //this.restTemplate=restTemplate;
        this.env=env;
        this.albumsServiceClient=albumsServiceClient;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        ModelMapper modelMapper=new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity =modelMapper.map(userDto,UserEntity.class);



        userRepository.save(userEntity);
        UserDto returnValue=modelMapper.map(userEntity,UserDto.class);
        return returnValue;

    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity=userRepository.findByEmail(email);
        if (userEntity==null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity=userRepository.findByUserId(userId);
        if (userEntity==null) throw new UsernameNotFoundException("User not found");
        UserDto userDto=new ModelMapper().map(userEntity,UserDto.class);
        String albumsUrl=String.format(env.getProperty("albums.url"),userId);
       /* ResponseEntity<List<AlbumResponseModel>> albumsListResponse =restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();*/

        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);

        userDto.setAlbums(albumsList);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity=userRepository.findByEmail(email);
        if (userEntity==null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),true ,true,true,true,new ArrayList<>());
    }
}
