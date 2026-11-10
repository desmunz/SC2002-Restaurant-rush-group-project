package restaurantrush.model.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items;

    public Menu() 
    {
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) 
    {
        if (item == null) 
        {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        items.add(item);
    }

    public List<MenuItem> getItems() 
    {
        return Collections.unmodifiableList(items);
    }

    public MenuItem getItemByName(String name) 
    {
        for (MenuItem item : items) 
        {
            if (item.getName().equalsIgnoreCase(name)) 
            {
                return item;
            }
        }
        return null;
    }
}
