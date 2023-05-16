//package PresentationLayer.views;
//
//import com.vaadin.flow.server.ServiceInitEvent;
//import com.vaadin.flow.server.VaadinServiceInitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ApplicationServiceInitListener implements VaadinServiceInitListener {
//
//    @Override
//    public void serviceInit(ServiceInitEvent event) {
////        event.addBootstrapListener(response -> {
////            // BoostrapListener to change the bootstrap page
////        });
//
//        event.addDependencyFilter((dependencies, filterContext) -> {
//            // DependencyFilter to add/remove/change dependencies sent to
//            // the client
//            System.out.println("Dependency filter");
//            return dependencies;
//        });
//
//        event.addRequestHandler((session, request, response) -> {
//            // RequestHandler to change how responses are handled
//            session.lock();
//            System.out.println(session.getSession().getId() + " session ID");
//            session.unlock();
//            return false;
//        });
//    }
//
//}