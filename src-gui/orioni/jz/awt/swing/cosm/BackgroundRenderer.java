package orioni.jz.awt.swing.cosm;

import java.awt.*;

/**
 * This interface is meant to be implemented by a source of background image data for a {@link Cosm}.  It provides
 * methods which are necessary to establish the background for a {@link CosmDisplayComponent}.  The purpose of this
 * manager is to abstract away the task of generating the background image, allowing it to be created in a variety of
 * ways.
 *
 * @author Zachary Palmer
 */
public interface BackgroundRenderer
{
// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Renders a background on the provided {@link Graphics} object.
     * @param g The {@link Graphics} object on which to render the background.
     * @param x The X offset of the upper left corner of the {@link Graphics} object.
     * @param y The Y offset of the upper left corner of the {@link Graphics} object.
     * @param width The width of the background to render.
     * @param height The height of the background to render.
     */
    public void renderBackground(Graphics g, int x, int y, int width, int height);
}
