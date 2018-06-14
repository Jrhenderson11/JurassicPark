package rendering;

public class SystemSettings {
	
	/**
	 * Native width of the display, this is the scale which World locations are on
	 */
	public static final int nativeWidth = 720;
	/**
	 * Native height of the display
	 */
	public static final int nativeHeight = 480;
	/**
	 * The width of the screen
	 */
    private static int screenWidth = 720;
    /**
     * The height of the screen
     */
    private static int screenHeight = 480;

    
    // -------
    // Methods
    // -------
    
    
    /**
     * Get the screen width setting
     * @return The screen's width
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Set the screen width to the given value
     * @param screenWidth The width to set the screen to.
     */
    public static void setScreenWidth(int screenWidth) {
        SystemSettings.screenWidth = screenWidth;
    }

    /**
     * Get the screen height setting
     * @return The screen's height
     */
    public static int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Set the screen height to the given value
     * @param screenHeight The height to set the screen to.
     */
    public static void setScreenHeight(int screenHeight) {
        SystemSettings.screenHeight = screenHeight;
    }

    /**
     * Get the native height setting
     * @return The native height
     */
    public static int getNativeHeight() {
        return nativeHeight;
    }
    
    /**
     * Get the screen width setting
     * @return The native width
     */
    public static int getNativeWidth() {
        return nativeWidth;
    }
    
    /**
     * Returns the scaled location of a vertical coordinate 'x' within the native dimensions
     * @param x The position within the native width
     * @return Returns x scaled up to the screen width.
     */
    public static double getScaledX(double x) {
    	return ((double) (SystemSettings.getScreenWidth() / (double) nativeWidth)) * x;
    }
    
    /**
     * Returns the scaled location of a horizontal coordinate 'y' within the native dimensions
     * @param y The position within the native height
     * @return Returns y scaled up to the screen height.
     */
    public static double getScaledY(double y) {
    	return ((double) (SystemSettings.getScreenHeight() / (double) nativeHeight)) * y;
    }
    
    /**
     * Returns the native location of a horizontal coordinate on the screen
     * @param x The x coordinate relative to the screen size
     * @return The coordinate scaled down to its native coordinates
     */
    public static double getDescaledX(double x) {
    	return ((double) (((double) nativeWidth) / SystemSettings.getScreenWidth())) * x;
    }
    
    /**
     * Returns the native location of a vertical coordinate on the screen
     * @param y The y coordinate relative to the screen size
     * @return The coordinate scaled down to its native coordinates
     */
    public static double getDescaledY(double y) {
    	return ((double) (((double) nativeHeight) / SystemSettings.getScreenHeight())) * y;
    }
}
