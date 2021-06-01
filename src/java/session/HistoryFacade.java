/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Furniture;
import entity.History;
import entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jvm
 */
@Stateless
public class HistoryFacade extends AbstractFacade<History> {

    @PersistenceContext(unitName = "WebBooksShoplPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoryFacade() {
        super(History.class);
    }

    public List<History> findReadingFurnitures(Customer customer) {
        try {
            return em.createQuery("SELECT h FROM History h WHERE h.returnDate = NULL AND h.customer = :customer")
                    .setParameter("customer", customer)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Furniture> findPurchasedFurniture(Customer customer) {
        try {
            return em.createQuery("SELECT h.book FROM History h WHERE h.customer = :customer")
                    .setParameter("customer", customer)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
