/*
 * Copyright (c) 2016 Amplience
 */
package com.amplience.hybris.dm.product.impl;

import com.amplience.hybris.dm.config.AmplienceConfigData;
import com.amplience.hybris.dm.config.AmplienceConfigService;
import com.amplience.hybris.dm.product.AmplienceIdentifierSanitizer;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import org.springframework.beans.factory.annotation.Required;

/**
 * Product Swatch Metadata UrlResolver for Amplience hosted product images
 */
public class AmplienceProductSwatchMetadataUrlResolver extends AbstractUrlResolver<ProductModel>
{
	private static final String CACHE_KEY = AmplienceProductSwatchMetadataUrlResolver.class.getName();
	private static final String SWATCH_SUFFIX = "-swatch";

	private AmplienceConfigService amplienceConfigService;
	private AmplienceIdentifierSanitizer amplienceIdentifierSanitizer;

	protected AmplienceConfigService getAmplienceConfigService()
	{
		return amplienceConfigService;
	}

	@Required
	public void setAmplienceConfigService(final AmplienceConfigService amplienceConfigService)
	{
		this.amplienceConfigService = amplienceConfigService;
	}

	protected AmplienceIdentifierSanitizer getAmplienceIdentifierSanitizer()
	{
		return amplienceIdentifierSanitizer;
	}

	@Required
	public void setAmplienceIdentifierSanitizer(final AmplienceIdentifierSanitizer amplienceIdentifierSanitizer)
	{
		this.amplienceIdentifierSanitizer = amplienceIdentifierSanitizer;
	}

	// --------

	@Override
	protected String getKey(final ProductModel source)
	{
		return CACHE_KEY + "." + source.getPk().toString();
	}

	@Override
	protected String resolveInternal(final ProductModel product)
	{
		final AmplienceConfigData amplienceConfig = getAmplienceConfigService().getConfigForCurrentSite();

		if (product != null)
		{
			return "https://" + amplienceConfig.getImageHostname() + "/i/" + amplienceConfig.getAccountIdentifier() + "/" + getImageIdentifierForProduct(product) + ".json";
		}
		return null;
	}

	protected String getImageIdentifierForProduct(final ProductModel product)
	{
		return getAmplienceIdentifierSanitizer().sanitize(product.getCode()) + SWATCH_SUFFIX;
	}
}
