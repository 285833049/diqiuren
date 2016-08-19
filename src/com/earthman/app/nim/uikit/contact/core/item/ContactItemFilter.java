package com.earthman.app.nim.uikit.contact.core.item;

import java.io.Serializable;

public interface ContactItemFilter extends Serializable {
	boolean filter(AbsContactItem item);
}
