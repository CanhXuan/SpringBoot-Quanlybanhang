package canhxuan.quanlybanhang.mapper;

import canhxuan.quanlybanhang.dto.CategoryDTO;
import canhxuan.quanlybanhang.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryDTO dto);
}
