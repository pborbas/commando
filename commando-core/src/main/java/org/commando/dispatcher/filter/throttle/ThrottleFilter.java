package org.commando.dispatcher.filter.throttle;

import org.commando.command.DispatchCommand;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.exception.TooFrequentExecutionException;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pborbas on 26/11/15.
 */
//TODO: move storage logic behind a repository interface

/**
 * Can controll the max execution/interval of commands
 * <p>
 * When a Command implements {@link org.commando.dispatcher.filter.throttle.ThrottledCommand} it counts the time between its executions for every CLEAN_INTERVAL_IN_MILLIS intervals
 * If during this interval the command is executed quicker than allowed it increases its hit counter.
 * If the hit counter goes above TOO_FREQUENT_HIT_THRESHOLD it block the command execution until the end of CLEAN_INTERVAL_IN_MILLIS interval,
 * then it resets the filter
 */
public class ThrottleFilter implements DispatchFilter {

	public static final int DEFAULT_CLEAN_INTERVAL = 30000;
	public static final int DEFAULT_HIT_THRESHOLD = 15;
	private final int cleanIntervalInMillis; //every N millis we release all blocked commands (clear hits)
	private final int tooFrequentHitThreshold; //it only blocks after N too frequent invocations

	private final Map<String, Long> execs = new ConcurrentHashMap();
	private final Map<String, AtomicInteger> hits = new ConcurrentHashMap();
	private final Timer cleanTimer;

	public ThrottleFilter() {
		this(DEFAULT_CLEAN_INTERVAL, DEFAULT_HIT_THRESHOLD);
	}

	public ThrottleFilter(int cleanIntervalInMillis, int tooFrequentHitThreshold) {
		this.cleanIntervalInMillis = cleanIntervalInMillis;
		this.tooFrequentHitThreshold = tooFrequentHitThreshold;
		this.cleanTimer = new Timer("ThrottlerCleanTimer");
		scheduleCleanup();
	}

	@Override
	public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain)
			throws DispatchException {
		if (dispatchCommand.getCommand() instanceof ThrottledCommand) {
			ThrottledCommand command = (ThrottledCommand) dispatchCommand.getCommand();
			long throttleTimeBetweenCallsInMillisecond = command.getThrottleTimeBetweenCallsInMillisecond();
			if (throttleTimeBetweenCallsInMillisecond > 0) {
				String key = command.getClass().getName() + "/" + command.getThrottleKey();
				AtomicInteger currentHit = hits.get(key);
				if (currentHit == null) {
					currentHit = new AtomicInteger(0);
					hits.put(key, currentHit);
				}
				if (currentHit.get() >= tooFrequentHitThreshold) {
					throw new TooFrequentExecutionException("Command " + command.getClass() + " is limited to 1 / "
							+ throttleTimeBetweenCallsInMillisecond + " milliseconds. Throttle key: " + key);
				}
				Long lastExec = execs.get(key);
				Long currentExec = System.currentTimeMillis();
				if (lastExec == null) {
					execs.put(key, currentExec);
				} else {
					if (currentExec - lastExec < throttleTimeBetweenCallsInMillisecond) {
						currentHit.incrementAndGet();
					} else {
						execs.put(key, currentExec);
					}
				}
			}
		}
		return filterChain.filter(dispatchCommand);
	}

	private void scheduleCleanup() {
		cleanTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				hits.clear();
				execs.clear();
			}
		}, cleanIntervalInMillis, cleanIntervalInMillis);
	}

	public int getCleanIntervalInMillis() {
		return cleanIntervalInMillis;
	}

	public int getTooFrequentHitThreshold() {
		return tooFrequentHitThreshold;
	}

}
