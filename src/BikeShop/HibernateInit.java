package BikeShop;

        import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Vishva on 3/20/2017.
 */
public class HibernateInit {
    private static final SessionFactory factory;
    static {
        try{
            factory = new Configuration().configure("bikeDB.xml").buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }

}
