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
