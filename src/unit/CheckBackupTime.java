package unit;

import java.time.LocalTime;

public class CheckBackupTime {
	private static final LocalTime TIME_FOR_2D_MORNING = LocalTime.of(13, 0, 0);
	private static final LocalTime TIME_FOR_2D_EVENING_AND_3D = LocalTime.of(17, 0, 0);

	public static boolean isOver1Hour() {
		return LocalTime.now().isAfter(TIME_FOR_2D_MORNING);
	}

	public static boolean isOver5Hour() {
		return LocalTime.now().isAfter(TIME_FOR_2D_EVENING_AND_3D);
	}

}
