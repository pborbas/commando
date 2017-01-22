package org.commando.json.serializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.commando.command.Command;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;

import java.io.IOException;

public class JsonSerializer implements Serializer {

	private ObjectMapper mapper;

	public JsonSerializer() {
		mapper = new ObjectMapper();
		mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	}

	@Override
	public String toText(Command<?> command) throws CommandSerializationException {
		return convertToText(command);
	}

	@Override
	public Command<?> toCommand(String textCommand) throws CommandSerializationException {
		return convertToObject(textCommand, Command.class);

	}

	@Override
	public Result toResult(String textResult) throws CommandSerializationException {
		return convertToObject(textResult, Result.class);
	}

	@Override
	public String toText(Result result) throws CommandSerializationException {
		return convertToText(result);
	}

	private String convertToText(Object object) throws CommandSerializationException {
		if (object != null) {
			try {
				return mapper.writeValueAsString(object);
			} catch (JsonProcessingException e) {
				throw new CommandSerializationException(
						"Error while converting " + object.getClass() + " to JSON: " + e, e);
			}
		} else {
			return "NULL";
		}
	}

	private <T> T convertToObject(String textCommand, Class<T> valueType) throws CommandSerializationException {
		if (textCommand != null) {
			try {
				return mapper.readValue(textCommand, valueType);
			} catch (IOException e) {
				throw new CommandSerializationException("Error while converting text to " + valueType + ": " + e, e);
			}
		} else
			return null;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
}
