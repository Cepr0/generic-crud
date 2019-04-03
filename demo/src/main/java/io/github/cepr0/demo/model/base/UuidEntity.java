/*
 * Copyright 2019 Generic-CRUD contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cepr0.demo.model.base;

import io.github.cepr0.crud.model.JpaEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class UuidEntity extends JpaEntity<UUID> {

	@Id private UUID id;
	@Version private Long version;

	public UuidEntity() {
		this.id = UUID.randomUUID();
	}

	@Override
	public int hashCode() {
		Assert.notNull(id, "id must not be null!");
		return id.hashCode();
	}
}
