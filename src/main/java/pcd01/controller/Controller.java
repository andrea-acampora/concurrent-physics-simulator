package pcd01.controller;

import pcd01.controller.concurrent.AbstractMasterAgent;

/**
 * The controller of the application.
 */
public interface Controller {

    /**
     * Starts the execution of the {@link AbstractMasterAgent}.
     */
    void execute();
}
