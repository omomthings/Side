package com.omomdevs.side.utils;

/**
 * Created by omaro on 01/11/2015.
 *
 * Notification Interface for different platforms, should implement it with the appropriate
 * java class launcher for each and catch back in you game object
 */
public interface NotificationListener {
    /** Will push a notification with the given params
     *
     * @param title is the title of the notification
     * @param text is the main text of the notification
     * @param sleep is the time in seconds to wait before the notification will be sent
     * @param androidIcon is the specific android icon code from the R file
     */
    void notify (String title,String text,int sleep,int androidIcon);

    /**Will push a notification with the default icon
     *
     * @param title is the title of the notification
     * @param text is the main text of the notification
     * @param sleep is the time in seconds to wait before the notification will be sent
     */
    void notify (String title,String text,int sleep);

    /**Will send a notification with the the default icon and immediately
     *
     * @param title is the title of the notification
     * @param text is the main text of the notification
     */
    void notify (String title,String text);

    /**Will send a notification with No text, with the default icon and immediately
     *
     * @param title the title of the notification
     */
    void notify (String title);
}
