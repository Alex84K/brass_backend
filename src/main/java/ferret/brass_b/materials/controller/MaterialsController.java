package ferret.brass_b.materials.controller;

import ferret.brass_b.materials.dto.CreateMaterialDto;
import ferret.brass_b.materials.dto.MaterialDto;
import ferret.brass_b.materials.service.MaterialService;
import ferret.brass_b.utils.PagedDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/materials")
public class MaterialsController {

    final MaterialService materialService;

    @PostMapping("/{publisherId}")
    public MaterialDto addNewMaterial(@PathVariable String publisherId, @RequestBody CreateMaterialDto newMaterial) {
        return materialService.addNewMaterial(publisherId, newMaterial);
    }

    @GetMapping("/{id}")
    public MaterialDto findMaterialById(@PathVariable String id) {
        return materialService.findMaterialById(id);
    }

    @DeleteMapping("/{id}")
    public MaterialDto removeMaterial(@PathVariable String id) {
        return materialService.removeMaterial(id);
    }

    @PutMapping("/{id}")
    public MaterialDto updateMaterial(@PathVariable String id, @RequestBody CreateMaterialDto newMaterial) {
        return materialService.updateMaterial(id, newMaterial);
    }

    @GetMapping("/publishers/{publisher}")
    public Iterable<MaterialDto> findMaterialsByPublisher(@PathVariable String publisher) {
        return materialService.findMaterialsByPublisher(publisher);
    }

    @GetMapping("/tags")
    public Iterable<MaterialDto> findMaterialsByTags(@RequestBody Set<String> tags) {
        return materialService.findMaterialsByTags(tags);
    }

    @GetMapping
    public PagedDataResponseDto<MaterialDto> findAll(
            @RequestParam(required = false, defaultValue = "25", name = "size") Integer size,
            @RequestParam(required = false, defaultValue = "0", name = "page") Integer page
    ) {
        return materialService.findAll(PageRequest.of(page, size));
    }
}
