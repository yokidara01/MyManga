package models;
import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
/**
 * Created by Aladinne on 12/02/2015.
 */
@Entity
public class Task extends Model {

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required
    public String name;

    public int done;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date dueDate = new Date();

    public static Finder<Long,Task> find = new Finder<Long,Task>(
            Long.class, Task.class
    );



}