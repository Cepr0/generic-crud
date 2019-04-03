package io.github.cepr0.crud.service;

import io.github.cepr0.crud.dto.ModelRequest;
import io.github.cepr0.crud.dto.ModelResponse;
import io.github.cepr0.crud.mapper.ModelMapper;
import io.github.cepr0.crud.model.Model;
import io.github.cepr0.crud.repo.ModelRepo;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ModelService extends AbstractCrudService<Model, Integer, ModelRequest, ModelResponse> {
	protected ModelService(@NonNull final ModelRepo repo, @NonNull final ModelMapper mapper) {
		super(repo, mapper);
	}
}
