package settings;

import play.GlobalSettings;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Global extends GlobalSettings {

    private static final Injector INJECTOR = Guice.createInjector(new IoCBinds());

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
      return INJECTOR.getInstance(controllerClass);
    }

    private static Injector createInjector() {
      return Guice.createInjector();
    }

  }
