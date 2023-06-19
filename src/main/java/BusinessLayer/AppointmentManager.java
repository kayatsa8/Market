package BusinessLayer;

import BusinessLayer.Stores.Appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentManager {
    private List<Appointment> appointmentsList;//<userid of user to appoint to owner, app=map<owner of this store,acceptOrNo>>
    public AppointmentManager(){
        appointmentsList =new ArrayList<>();
    }
    public void addAppointment(Appointment appoint) throws Exception {
        try {
            appointmentsList.add(appoint);
        }catch (Exception e){
            throw new Exception("Failed to addAppointment"+e.getMessage());
        }
    }

    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
    }

    public void removeAppointment(int userId) throws Exception {
        appointmentsList.remove(getAppointmentByNewOwnerId(userId));
    }

    public void accept(int myId, int theNewOwnerId) throws Exception {
        Appointment appointment=getAppointmentByNewOwnerId(theNewOwnerId);
        if (appointment==null)
            throw new Exception("cant find this appointment");
        appointment.accept(myId);
    }
    public boolean isAllAccepted(int newOwnerId) throws Exception {
        Appointment appointment=getAppointmentByNewOwnerId(newOwnerId);
        if (appointment==null)
            throw new Exception("cant find this appointment");
        else
            return !appointment.getAcceptMap().containsValue(false);//at least one not yet accepted
    }

    public int getStoreId(int newOwnerId) throws Exception {
        return getAppointmentByNewOwnerId(newOwnerId).getStoreId();
    }
    private Appointment getAppointmentByNewOwnerId(int newOwnerId) throws Exception {
        List<Appointment> appointmentList=appointmentsList.stream().filter(appointment -> appointment.getNewOwnerId()==newOwnerId).toList();
        if (!appointmentList.isEmpty())
            return appointmentsList.get(0);
        else throw new Exception("id not found");
    }
}
