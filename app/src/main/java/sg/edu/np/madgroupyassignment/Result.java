package sg.edu.np.madgroupyassignment;


import java.util.List;

public class Result {

    public List<HCCRoute> routes;
    public String status;

    public List<HCCRoute> getRoutes() {return routes;}

    public void setRoutes(List<HCCRoute> routes) {
        this.routes = routes;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {
        this.status = status;
    }
}

