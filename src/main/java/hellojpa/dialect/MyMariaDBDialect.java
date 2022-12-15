package hellojpa.dialect;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyMariaDBDialect extends MariaDB103Dialect {

    public MyMariaDBDialect() {
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
