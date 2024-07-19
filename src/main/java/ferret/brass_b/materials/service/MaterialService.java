package ferret.brass_b.materials.service;

import ferret.brass_b.accouting.dto.UserResponseDto;
import ferret.brass_b.materials.dto.CreateMaterialDto;
import ferret.brass_b.materials.dto.MaterialDto;
import ferret.brass_b.utils.PagedDataResponseDto;
import org.springframework.data.domain.PageRequest;

import java.util.Set;

public interface MaterialService {
    MaterialDto addNewMaterial(String publisher, CreateMaterialDto newMaterial);

    MaterialDto findMaterialById(String id);

    MaterialDto removeMaterial(String id);

    MaterialDto updateMaterial(String id, CreateMaterialDto newMaterial);

    Iterable<MaterialDto> findMaterialsByPublisher(String publisher);

    Iterable<MaterialDto> findMaterialsByTags(Set<String> tags);

    PagedDataResponseDto<MaterialDto> findAll(PageRequest pageRequest);
}
