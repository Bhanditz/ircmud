package com.cb2.ircmud.domain.containers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.cb2.ircmud.domain.Item;
import com.cb2.ircmud.domain.World;
import com.cb2.ircmud.event.Event;
import com.cb2.ircmud.event.EventListener;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "SINGLE_TABLE")
public abstract class Container implements EventListener {

	@ManyToOne
	World world;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy="location")
    private List<Item> items = new ArrayList<Item>();
    
    public void handleEvent(Event event) {
    	for (Item i : items) i.handleEvent(event);
    }
    
    
    public void addItem(Item item) {
    	items.add(item);
    }
    public boolean removeItem(Item item) {
    	return items.remove(item);
    }
    
}
