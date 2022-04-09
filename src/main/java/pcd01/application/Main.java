package pcd01.application;

import gov.nasa.jpf.vm.Verify;
import pcd01.controller.*;
import pcd01.model.Model;
import pcd01.model.ModelImpl;

public class Main {

    public static void main(String[] args) {
        int numberOfBodies = Verify.getInt(1,3);
        Model model = new ModelImpl(numberOfBodies);
        Controller controller = new Controller(model);
    }
}
