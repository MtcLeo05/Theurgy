package com.leo.theurgy.api.aspectus;

import com.leo.theurgy.api.data.aspectus.Aspectus;

public interface IAspectusHolder {

    int maxAspectus(IAspectusHolderContext context);
    Aspectus aspectusType(IAspectusHolderContext context);
    int currentAspectus(IAspectusHolderContext context);

}
