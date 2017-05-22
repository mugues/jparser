package it.sample.parser.core.common;

import it.sample.parser.domain.annotation.Verify;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.util.ReflectionUtils.invokeMethod;

public class DomainVerifier<T> implements ObjectVerifier
{
  private Class<T> persistentClass;

  public DomainVerifier(Class<T> persistentClass) {
    super();
    this.persistentClass = persistentClass;
  }

  @Override
  public Collection<String> verify(Object object, Object object1) {

    Collection<String> elements = new ArrayList<>();
    try {

      Field[] fields = persistentClass.getDeclaredFields();


      for (Field field : fields)
      {
        Verify fieldToVerify = field.getAnnotation(Verify.class);

        if (fieldToVerify != null)
        {
          Method method = field.getType().getMethod("equals", Object.class);
          Field declaredField = object.getClass().getDeclaredField(field.getName());
          Field declaredField1 = object1.getClass().getDeclaredField(field.getName());

          declaredField.setAccessible(true);
          Object o = declaredField.get(object);

          declaredField1.setAccessible(true);
          Object o1 = declaredField1.get(object1);

          if( o != null)
          {
            boolean result = (boolean) method.invoke(o, o1);

            if (!result)
            {
              elements.add(declaredField.getName());
            }
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return elements;

  }
}
