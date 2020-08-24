package com.spring.os.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.spring.os.domain.model.Cliente;
import com.spring.os.domain.repository.ClienteRepository;
import com.spring.os.domain.service.CadastroClienteService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository; //instancia de repository

	@Autowired
	private CadastroClienteService cadastroCliente;//instancia de cadastro Cliente

	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);

		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get()); // se tiver algum status OK 200
		}

		return ResponseEntity.notFound().build(); // se não tiver apenas status 404
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) //Resposte o status  created 201
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {  //@ResqueBody Corpo do cliente passado no body
																	// @valid ativa a validação
		return cadastroCliente.salvar(cliente);
	}

	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long clienteId, @RequestBody Cliente cliente) {

		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build(); // retorna status 404
		}

		cliente.setId(clienteId);// atribuição de cliente para a atualização
		cliente = cadastroCliente.salvar(cliente);

		return ResponseEntity.ok(cliente); // retorna status 200
	}

	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> remover(@PathVariable Long clienteId) { // @pathVariable id do cliente passado no requição
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}

		cadastroCliente.excluir(clienteId);

		return ResponseEntity.noContent().build(); // retorno status 204 , sem corpo pois é um metodo void
	}

}
