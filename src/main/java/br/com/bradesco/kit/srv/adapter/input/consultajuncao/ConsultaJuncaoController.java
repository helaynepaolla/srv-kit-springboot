package br.com.bradesco.kit.srv.adapter.input.consultajuncao;

import br.com.bradesco.kit.srv.domain.entity.Juncao;
import br.com.bradesco.kit.srv.port.input.ConsultarJuncaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@RestController
public class ConsultaJuncaoController {

	private ConsultarJuncaoUseCase juncaoUseCase;

	@Autowired
	public ConsultaJuncaoController(
			@NonNull ConsultarJuncaoUseCase juncaoUseCase) {
		this.juncaoUseCase = juncaoUseCase;
	}

	@GetMapping("/frwk/{rotina}/{matricula}")
	public Juncao frwk(
			@PathVariable("rotina") @Pattern(regexp = "[a-zA-Z0-9]{4}") String rotina,
			@PathVariable("matricula") @Min(1000000) @Max(99999999) Integer matricula) {

		return juncaoUseCase
				.getJuncaoPertence(rotina, matricula)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
}
