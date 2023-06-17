package superapp.dal;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import superapp.data.MiniAppCommandEntity;
import superapp.logic.MiniAppCommandQueries;
import superapp.restAPI.*;

@Service
public class MiniAppCommandServiceDB implements MiniAppCommandQueries {

	private MiniAppCommandCRUD miniAppCommandsCRUD;
	private ObjectMapper jackson;
	private JmsTemplate jmsTemplate;

	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		this.jmsTemplate.setDeliveryDelay(5000L);
	}

	@Autowired
	public void setMiniAppCommandsCRUD(MiniAppCommandCRUD miniAppCommandsCRUD) {
		this.miniAppCommandsCRUD = miniAppCommandsCRUD;
	}

	@PostConstruct
	public void setup() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public MiniAppCommandBoundary invokeCommand(MiniAppCommandBoundary command) {

		validateCommand(command);
		MiniAppCommandEntity newCommand = this.toEntity(command);

		newCommand = this.miniAppCommandsCRUD.save(newCommand);
						
		return this.toBoundary(newCommand);
		
	}
	
	
	@Deprecated
	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {

		return this.miniAppCommandsCRUD.findAll().stream() // Stream<MiniAppCommandEntity>
				.map(this::toBoundary) // Stream<MiniAppCommandBoundary>
				.toList(); // List<MiniAppCommandBoundary>
	}

	@Deprecated
	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {

		return this.miniAppCommandsCRUD.findAll().stream() // Stream<MiniAppCommandEntity>
				.filter(t -> t.getCommandId().getMiniapp().equals(miniAppName)).map(this::toBoundary) // Stream<MiniAppCommandBoundary>
				.toList(); // List<MiniAppCommandBoundary>

	}

	private MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {

		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();

		boundary.setCommandId(entity.getCommandId());
		boundary.setCommand(entity.getCommand());
		boundary.setTargetObject(entity.getTargetObject());
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
		boundary.setInvokedBy(entity.getInvokedBy());
		boundary.setCommandAttributes(entity.getCommandAttributes());

		return boundary;
	}

	private MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {

		MiniAppCommandEntity entity = new MiniAppCommandEntity();

		entity.setCommandId(boundary.getCommandId());
		entity.setCommand(boundary.getCommand());
		entity.setInvocationTimestamp(new Date());
		// newCommand.setInvocationTimestamp(new Date());

		if (boundary.getTargetObject() != null) {
			entity.setTargetObject(boundary.getTargetObject());
		}

		if (boundary.getInvocationTimestamp() != null) {
			entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		}

		if (boundary.getInvokedBy() != null) {
			entity.setInvokedBy(boundary.getInvokedBy());
		}

		if (boundary.getCommandAttributes() != null) {
			entity.setCommandAttributes(boundary.getCommandAttributes());
		}

		return entity;
	}

	@Override
	public void deleteAllCommands() {
		this.miniAppCommandsCRUD.deleteAll();
	}

	private void validateCommand(MiniAppCommandBoundary miniAppCommandBoundary) {

		if (isNullOrEmpty(miniAppCommandBoundary.getCommand())) {
			throw new IllegalArgumentException("Command must be provided and not be empty");
		}

		if (miniAppCommandBoundary.getInvokedBy().getUserId() == null) {
			throw new IllegalArgumentException("User who activated the command must be provided");
		}

		if (miniAppCommandBoundary.getTargetObject() == null) {
			throw new IllegalArgumentException("ID of the SuperAppObject the command is activated on must be provided");
		}
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	@Override
	public MiniAppCommandBoundary invokeCommandLater(MiniAppCommandBoundary command) {

		validateCommand(command);
		MiniAppCommandEntity newCommand = this.toEntity(command);

		try {
			String json = this.jackson.writeValueAsString(newCommand);
			this.jmsTemplate.convertAndSend("miniAppCommand", json);
			return this.toBoundary(newCommand);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@JmsListener(destination = "miniAppCommand")
	public void lisenToCommandInvoke(String json) {
		try {
			// delay 3 second
			TimeUnit.SECONDS.sleep(10);
			System.err.println("*** received: " + json);
			MiniAppCommandBoundary command = this.jackson.readValue(json, MiniAppCommandBoundary.class);

			MiniAppCommandEntity entity = this.toEntity(command);
			entity = this.miniAppCommandsCRUD.save(entity);
			System.err.println("*** saved: " + this.toBoundary(entity));
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	@Override
	public List<MiniAppCommandBoundary> getAllCommandsWithPagination(int page, int size) {
		return this.miniAppCommandsCRUD.findAll(PageRequest.of(page, size, Direction.ASC, "command")).stream()
				.map(this::toBoundary).toList();

	}

	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommandsWithPagination(String miniAppName, int page, int size) {
		return this.miniAppCommandsCRUD.findAll(PageRequest.of(page, size, Direction.ASC, "command")).stream() // Stream<MiniAppCommandEntity>
				.filter(t -> t.getCommandId().getMiniapp().equals(miniAppName)).map(this::toBoundary) // Stream<MiniAppCommandBoundary>
				.toList(); // List<MiniAppCommandBoundary>
	}

}
