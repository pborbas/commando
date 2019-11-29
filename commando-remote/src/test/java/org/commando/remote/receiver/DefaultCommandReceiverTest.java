package org.commando.remote.receiver;

import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.TooFrequentExecutionException;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import org.commando.result.ResultFuture;
import org.commando.result.VoidResult;
import org.commando.testbase.command.TestWaitCommand;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 *
 */
public class DefaultCommandReceiverTest {

	@Test
	public void testNormalExecution() throws Exception {
		Serializer mockSerializer = mock(Serializer.class);
		TestWaitCommand testWaitCommand = new TestWaitCommand(1l);
		when(mockSerializer.toCommand(Mockito.anyString())).thenReturn((Command) testWaitCommand);
		when(mockSerializer.toText(any(Result.class))).thenReturn("mock result");
		Dispatcher mockDispatcher = mock(Dispatcher.class);
		ResultFuture<Result> resultFuture = new ResultFuture<>(0);
		resultFuture.complete(new VoidResult());
		when(mockDispatcher.dispatch(any(Command.class))).thenReturn(resultFuture);
		DefaultCommandReceiver commandReceiver = new DefaultCommandReceiver(mockSerializer, mockDispatcher);
		Map<String, String> headers = new HashMap<String, String>() {{
			put("a", "1");
			put("b", "2");
		}};
		TextDispatcherCommand textDispatcherCommand=new TextDispatcherCommand("command data", headers);
		TextDispatcherResult textDispatcherResult = commandReceiver.execute(textDispatcherCommand).get();
		assertEquals("mock result", textDispatcherResult.getTextResult()); //returned serialized dispatcher result
		ArgumentCaptor<Command> commandArgumentCaptor = ArgumentCaptor.forClass(Command.class);
		verify(mockDispatcher).dispatch(commandArgumentCaptor.capture());
		assertEquals(2, commandArgumentCaptor.getValue().getHeaders().size()); //headers were passed
	}

	@Test
	public void testExceptionalExecution() throws Exception {
		Serializer mockSerializer = mock(Serializer.class);
		TestWaitCommand testWaitCommand = new TestWaitCommand(1l);
		when(mockSerializer.toCommand(Mockito.anyString())).thenReturn((Command) testWaitCommand);
		when(mockSerializer.toText(any(Result.class))).thenReturn("mock result");
		Dispatcher mockDispatcher = mock(Dispatcher.class);
		ResultFuture<Result> resultFuture = new ResultFuture<>(0);
		TooFrequentExecutionException exception = new TooFrequentExecutionException("mock exception");
		resultFuture.completeExceptionally(exception);
		when(mockDispatcher.dispatch(any(Command.class))).thenReturn(resultFuture);
		DefaultCommandReceiver commandReceiver = new DefaultCommandReceiver(mockSerializer, mockDispatcher);
		Map<String, String> headers = new HashMap<String, String>() {{
			put("a", "1");
			put("b", "2");
		}};
		TextDispatcherCommand textDispatcherCommand=new TextDispatcherCommand("command data", headers);
		TextDispatcherResult textDispatcherResult = commandReceiver.execute(textDispatcherCommand).get();
		System.out.println(textDispatcherResult.getTextResult());

		assertEquals("mock exception", textDispatcherResult.getTextResult()); //body contains exception message
		assertEquals("org.commando.exception.TooFrequentExecutionException", textDispatcherResult.getHeader(
				RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS)); //header has the exception class
	}
}