package DataAccessLayer;

import BusinessLayer.Market;
import BusinessLayer.Stores.Appointment;
import BusinessLayer.Stores.CatalogItem;
import BusinessLayer.Stores.Store;
import DataAccessLayer.Hibernate.ConnectorConfigurations;
import DataAccessLayer.Hibernate.DBConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//DB mock
public class AppointmentDAO {
    ConnectorConfigurations config;
    public AppointmentDAO() {
        config = Market.getConfigurations();
    }
    public DBConnector<Appointment> getConnector() {
        return new DBConnector<>(Appointment.class, config);
    }


    public void addAppointment(Appointment appointment) {
        getConnector().insert(appointment);
    }

    public void removeAppointment(Appointment appointment) throws Exception {
//        getConnector().d(appointment.getStoreId());
    }

    public Set<Appointment> getAppointments() {
        Set<Appointment> appointments = (Set<Appointment>) getConnector().getAll();
        return appointments;
    }

    public void save(Appointment appointment) {
        getConnector().saveState(appointment);
    }
}
