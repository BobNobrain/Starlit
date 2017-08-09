const dom = (function ()
{
	'use strict';
	let dom = function (tagName, options, content)
	{
		var result = document.createElement(tagName);
		if (options && typeof options === typeof {})
		{
			for (var key in options)
			{
				if (!options.hasOwnProperty(key)) continue;
				result.setAttribute(key, options[key].toString());
			}
		}
		if (content)
		{
			appendContent(result, content);
		}
		return result;
	}
	let appendContent = function (el, content)
	{
		if (typeof content === typeof '')
			el.innerHTML = content;
		else if (content instanceof HTMLElement)
			el.appendChild(content);
		else if (Array.isArray(content))
			content.forEach(function (c) { appendContent(el, c); });
	}

	return dom;
})();

const Fields = {
	"name":				{ label: "Name", 					type: "string" },
	"desc":				{ label: "Description",				type: "text" },
    "price": 			{ label: "Price", 					type: "price" },
    "destruct_price": 	{ label: "Destruction Price", 		type: "price" },
    "population": 		{ label: "Population", 				type: "number" },
    "time": 			{ label: "Time", 					type: "time" },
    "place": 			{ label: "Place", 					type: "place" },
    "tech_level": 		{ label: "Tech Level",				type: "tech_level" },
    "class": 			{ label: "Class", 					type: "jclass" },
    "storage_limit": 	{ label: "Storage Limit",			type: "storage_limit" },
    "plant_speed": 		{ label: "Plant Speed",				type: "price" },
    "plant_consumption":{ label: "Plant Consumption",		type: "price" },
    "ship_price": 		{ label: "Ship Price",				type: "price" }
};

function getFieldDescription (fname, val)
{
	if (fname in Fields) return Fields[fname];
	return {
		label: fname,
		type: typeof val
	};
}
