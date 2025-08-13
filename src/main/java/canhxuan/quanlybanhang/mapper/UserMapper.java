package canhxuan.quanlybanhang.mapper;

import canhxuan.quanlybanhang.dto.LoginRequest;
import canhxuan.quanlybanhang.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
     User toUser(LoginRequest request);
}
