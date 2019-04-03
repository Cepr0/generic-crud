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

package io.github.cepr0.crud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.Instant;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "history")
public class Event extends IntIdEntity {

	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 6)
	private Type type;

	@Column(nullable = false)
	private Integer modelId;

	@Column(nullable = false, length = 32)
	private String modelText;

	@Column(nullable = false, length = 32)
	private Integer modelNumber;

	public Event(@NonNull final Type type, @NonNull final Model model) {
		this.type = type;
		this.modelId = model.getId();
		this.modelText = model.getText();
		this.modelNumber = model.getNumber();
	}

	public enum Type {
		CREATE, UPDATE, DELETE
	}
}
