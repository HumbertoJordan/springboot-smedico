package com.imb2025.smedico.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imb2025.smedico.dto.ApiResponseSuccessDto;
import com.imb2025.smedico.dto.request.EncuestaRequestDto;
import com.imb2025.smedico.dto.response.EncuestaResponseDto;
import com.imb2025.smedico.entity.Encuesta;
import com.imb2025.smedico.service.IEncuestaService;
import com.imb2025.smedico.mapper.EncuestaMapper;

@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

        @Autowired
        private IEncuestaService service;

        @Autowired
        private EncuestaMapper mapper;

        @GetMapping
        public ResponseEntity<ApiResponseSuccessDto<List<EncuestaResponseDto>>> findAll() {
                List<Encuesta> lista = service.findAll();
                List<EncuestaResponseDto> listaResponse = new ArrayList<>(lista.size());
                for (Encuesta e : lista) {
                        listaResponse.add(mapper.toDto(e));
                }
                ApiResponseSuccessDto<List<EncuestaResponseDto>> resp = new ApiResponseSuccessDto<>(true,
                                listaResponse.isEmpty() ? "No hay encuestas disponibles" : "Listado de encuestas",
                                listaResponse);
                return ResponseEntity.ok(resp);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseSuccessDto<EncuestaResponseDto>> findById(@PathVariable("id") Long id) {
                Encuesta e = service.findById(id);
                EncuestaResponseDto dto = mapper.toDto(e);
                ApiResponseSuccessDto<EncuestaResponseDto> resp = new ApiResponseSuccessDto<>(true,
                                "Encuesta encontrada", dto);
                return ResponseEntity.ok(resp);
        }

        @PostMapping
        public ResponseEntity<ApiResponseSuccessDto<EncuestaResponseDto>> create(
                        @Valid @RequestBody EncuestaRequestDto body) {
                Encuesta creada = service.create(mapper.fromDto(body));
                ApiResponseSuccessDto<EncuestaResponseDto> resp = new ApiResponseSuccessDto<>(true,
                                "Encuesta creada correctamente", mapper.toDto(creada));
                return ResponseEntity.created(URI.create("/api/encuestas/" + creada.getId())).body(resp);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseSuccessDto<EncuestaResponseDto>> update(@PathVariable("id") Long id,
                        @Valid @RequestBody EncuestaRequestDto body) {
                Encuesta actualizada = service.update(id, mapper.fromDto(body));
                ApiResponseSuccessDto<EncuestaResponseDto> resp = new ApiResponseSuccessDto<>(true,
                                "Encuesta actualizada correctamente", mapper.toDto(actualizada));
                return ResponseEntity.ok(resp);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponseSuccessDto<String>> delete(@PathVariable("id") Long id) {
                service.deleteById(id);
                ApiResponseSuccessDto<String> resp = new ApiResponseSuccessDto<>(true,
                                "Encuesta eliminada correctamente", "Id: " + id);
                return ResponseEntity.ok(resp);
        }

        @GetMapping("/min-puntaje/{n}")
        public ResponseEntity<ApiResponseSuccessDto<List<EncuestaResponseDto>>> findBycalificacionLessThan(@PathVariable int n) {
                List<EncuestaResponseDto> data = service.findByCalificacionLessThanEqual(n)
                                .stream()
                                .map(mapper::toDto)
                                .collect(Collectors.toList());
                ApiResponseSuccessDto<List<EncuestaResponseDto>> resp = new ApiResponseSuccessDto<>(true,
                                "Encuestas con puntaje >= " + n, data);
                return ResponseEntity.ok(resp);
        }

        @GetMapping("/max-puntaje/{n}")
        public ResponseEntity<ApiResponseSuccessDto<List<EncuestaResponseDto>>> findByCalificacionGreaterThan(@PathVariable int n) {
                List<EncuestaResponseDto> data = service.findByCalificacionGreaterThan(n)
                                .stream()
                                .map(mapper::toDto)
                                .collect(Collectors.toList());
                ApiResponseSuccessDto<List<EncuestaResponseDto>> resp = new ApiResponseSuccessDto<>(true,
                                "Encuestas con puntaje >= " + n, data);
                return ResponseEntity.ok(resp);
        }

}
