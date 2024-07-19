package ferret.brass_b.materials.service;

import ferret.brass_b.materials.dao.MaterialsRepository;
import ferret.brass_b.materials.dto.CreateMaterialDto;
import ferret.brass_b.materials.dto.MaterialDto;
import ferret.brass_b.materials.dto.exceptions.MaterialNotFoundException;
import ferret.brass_b.materials.model.Material;
import ferret.brass_b.utils.PagedDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService{

    final MaterialsRepository materialsRepository;
    final ModelMapper modelMapper;

    @Override
    public MaterialDto addNewMaterial(String publisher, CreateMaterialDto newMaterial) {
        Material material = modelMapper.map(newMaterial, Material.class);
        material.setPublisher(publisher);
        material = materialsRepository.save(material);
        return modelMapper.map(material, MaterialDto.class);
    }

    @Override
    public MaterialDto findMaterialById(String id) {
        Material material = materialsRepository.findById(id).orElseThrow(MaterialNotFoundException::new);
        return modelMapper.map(material, MaterialDto.class);
    }

    @Override
    public MaterialDto removeMaterial(String id) {
        Material material = materialsRepository.findById(id).orElseThrow(MaterialNotFoundException::new);
        materialsRepository.delete(material);
        return modelMapper.map(material, MaterialDto.class);
    }

    @Override
    public MaterialDto updateMaterial(String id, CreateMaterialDto newMaterial) {
        Material material = materialsRepository.findById(id).orElseThrow(MaterialNotFoundException::new);
        String link = newMaterial.getLink();
        if(link != null){
            material.setLink(link);
        }
        String title = newMaterial.getTitle();
        if (title != null) {
            material.setTitle(title);
        }
        Set<String> tags = newMaterial.getTags();
        if (tags != null) {
            tags.forEach(material::addTag);
        }
        materialsRepository.save(material);
        return modelMapper.map(material, MaterialDto.class);
    }

    @Override
    public Iterable<MaterialDto> findMaterialsByPublisher(String publisher) {
        return materialsRepository.findByPublisherIgnoreCase(publisher)
                .map(m -> modelMapper.map(m, MaterialDto.class))
                .toList();
    }

    @Override
    public Iterable<MaterialDto> findMaterialsByTags(Set<String> tags) {
        return materialsRepository.findByTagsInIgnoreCase(tags)
                .map(m -> modelMapper.map(m, MaterialDto.class))
                .toList();
    }

    @Override
    public PagedDataResponseDto<MaterialDto> findAll(PageRequest pageRequest) {
        Page<Material> materials = materialsRepository.findAll(pageRequest);
        PagedDataResponseDto<MaterialDto> materialDataDto = new PagedDataResponseDto<>();
        Page<MaterialDto> pageMapped = materials.map(m -> modelMapper.map(m, MaterialDto.class));
        materialDataDto.setData(pageMapped.getContent());
        materialDataDto.setTotalElements(pageMapped.getTotalElements());
        materialDataDto.setTotalPages(pageMapped.getTotalPages());
        materialDataDto.setCurrentPage(pageMapped.getNumber());
        return materialDataDto;
    }
}
