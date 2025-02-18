package br.edu.ifpb.es.daw.todo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "uc_descricao_tarefa", columnNames = {"descrição"})
})
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(nullable = false)
	private UUID lookupId;
	
	@Column(nullable = false)
	private String descrição;
	
	private LocalDateTime criadoEm;
	
	private LocalDateTime concluídoEm;
	
	@PrePersist
	private void init() {
		this.lookupId = UUID.randomUUID();
		this.criadoEm = LocalDateTime.now();
	}

	public boolean feito() {
		return this.concluídoEm != null;
	}
	
}
