'use strict';

class BuildingRecord
{
	constructor(data)
	{
		this.data = data;
		this.editors = {};
		this.el = this.createElement();
	}

	createElement()
	{
		const trs = [];

		for (let key in this.data)
		{
			if (!this.data.hasOwnProperty(key)) continue;
			trs.push(this.addField(key, this.data[key]));
		}

		const controls = [
			dom('button', { type: 'button' }, 'Remove'),
			dom('button', { type: 'button' }, 'Move Up'),
			dom('button', { type: 'button' }, 'Move Down'),
			dom('button', { type: 'button' }, 'Duplicate'),
			dom('input', { type: 'text', value: '' })
			dom('button', { type: 'button' }, 'Add Field')
		];
		controls[0].addEventListener('click', () => removeRecord(this));
		controls[1].addEventListener('click', () => moveRecordUp(this));
		controls[2].addEventListener('click', () => moveRecordDown(this));
		controls[3].addEventListener('click', () => duplicateRecord(this));
		controls[5].addEventListener('click', () =>
		{
			const name = prompt('Enter field name:');
			if (name)
			{
				if (name in this.data)
					return alert('This field already exists!');

				const tr = this.addField(name, void 0);
				$(tr).insertAfter($(this.el).find('table tr:last-child'));
			}
		});

		const root = dom('div', { 'class': 'building' }, [
			dom('table', null, trs),
			dom('div', { 'class': 'controls' }, controls)
		]);
		return root;
	}

	updateData()
	{
		for (let key in this.editors)
		{
			this.data[key] = this.editors[key].getValue();
		}
	}

	addField(fieldName, fieldValue)
	{
		const fieldDesc = getFieldDescription(fieldName, fieldValue);

		const editor = editors.forName(fieldDesc.type);

		if (fieldValue === void 0)
			fieldValue = editor.defaultValue();

		const valueTd = dom('td', { 'class': fieldDesc.type });
		editor.attach(valueTd);
		editor.setValue(fieldValue);
		this.editors[fieldName] = editor;

		const labelTd = dom('td', null, fieldDesc.label);
		const remBtn = dom('a', { href: '#' }, 'Remove');
		remBtn.addEventListener('click', ev =>
		{
			this.removeField(fieldName);
			ev.preventDefault();
			return false;
		});

		const tr = dom('tr', null, [labelTd, valueTd, dom('td', null, remBtn)]);
		return tr;
	}

	removeField(fieldName)
	{
		delete this.data[fieldName];
		$(this.editors[fieldName].element).closest('tr').remove();
		delete this.editors[fieldName];
	}
}

const editors = (function ()
{
	'use strict';

	class Editor
	{
		constructor(type)
		{
			this.type = type;
			this.value = void 0;
			this.element = null;
		}

		attach(element)
		{
			this.element = element;
		}

		getValue()
		{
			return this.value;
		}

		setValue(val)
		{
			this.value = val;
		}

		defaultValue()
		{
			return void 0;
		}
	}


	const registry = {};
	const forName = name =>
	{
		if (name in registry)
			return new registry[name]();
		return new Editor(name);
	}
	const register = (name, editorClass) =>
	{
		registry[name] = editorClass;
	}

	return {
		forName,
		register,
		Editor
	};
})();

editors.register('string', class StringEditor extends editors.Editor
{
	constructor(type = 'string')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.input = dom('input', { type: 'text', value: '' });
		element.appendChild(this.input);

		// this.input.addEventListener('change', () =>
		// {
		// 	this.value = this.input.value;
		// });
	}
	getValue()
	{
		return this.input.value;
	}
	setValue(val)
	{
		this.input.value = val;
	}
	defaultValue() { return ''; }
});

editors.register('text', class TextEditor extends editors.Editor
{
	constructor(type = 'text')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.input = $('<textarea></textarea>');
		element.appendChild(this.input[0]);
	}
	getValue()
	{
		return this.input.val();
	}
	setValue(val)
	{
		this.input.val(val);
	}
	defaultValue() { return ''; }
});

class NumberEditor extends editors.Editor
{
	constructor(type = 'number')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.input = dom('input', { type: 'number', value: 0, 'step': 0.0000001 });
		element.appendChild(this.input);

		// this.input.addEventListener('change', () =>
		// {
		// 	this.value = this.input.value;
		// });
	}
	getValue()
	{
		return parseFloat(this.input.value);
	}
	setValue(val)
	{
		this.input.value = parseFloat(val);
	}
	defaultValue() { return 0; }
}
editors.register('number', NumberEditor);

editors.register('price', class PriceEditor extends editors.Editor
{
	constructor(type = 'price')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.inputs = [];

		let keys = Object.keys(window.data.res_types);

		for (let i = 0; i < keys.length; i++)
		{
			let input = { name: keys[i], icon: window.data.res_types[keys[i]] };
			input.el = dom('input', { type: 'number', value: 0, title: input.name });
			this.inputs.push(input);
			element.appendChild(input.el);
			element.appendChild(dom('span', null, input.icon));
		}
	}
	getValue()
	{
		return this.inputs.map(i => parseInt(i.el.value));
	}
	setValue(val)
	{
		for (let i = 0; i < val.length; i++)
		{
			this.inputs[i].el.value = val[i];
		}
	}
	defaultValue() { return [0,0,0,0,0,0,0]; }
});

editors.register('time', class TimeEditor extends NumberEditor
{
	constructor(type = 'time')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.element.appendChild(dom('span', null, 'ms'));
		this.input.step = 1;
	}
});

editors.register('place', class PlaceEditor extends editors.Editor
{
	constructor(type = 'place')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.inputs = [];

		const places = window.data.places;
		places.forEach(place =>
		{
			const input = dom('input', { type: 'checkbox', 'data-place': place });
			this.inputs.push(input);

			element.appendChild(
				dom('label', null, [
					input,
					dom('span', null, place)
				])
			);
		});
	}
	getValue()
	{
		return this.inputs.reduce((acc, item) => item.checked? [item.getAttribute('data-place'), ...acc] : acc, []);
	}
	setValue(val)
	{
		for (let i = 0; i < this.inputs.length; i++)
		{
			this.inputs[i].checked = val.indexOf(this.inputs[i].getAttribute('data-place')) != -1;
		}
	}
	defaultValue() { return []; }
});

editors.register('storage_limit', class StorageLimitEditor extends editors.Editor
{
	constructor(type = 'storage_limit')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);
		this.inputs = [];

		let keys = Object.keys(window.data.res_classes);

		for (let i = 0; i < keys.length; i++)
		{
			const resClass = window.data.res_classes[keys[i]];
			const input = { name: keys[i], icon: resClass.icon };
			input.el = dom('input', { type: 'number', value: 0, title: `${input.name} (${resClass.types.join(', ')})` });
			this.inputs.push(input);
			element.appendChild(input.el);
			element.appendChild(dom('span', null, input.icon));
		}
	}
	getValue()
	{
		return this.inputs.map(i => parseInt(i.el.value));
	}
	setValue(val)
	{
		for (let i = 0; i < val.length; i++)
		{
			this.inputs[i].el.value = val[i];
		}
	}
	defaultValue() { return [0,0,0,0]; }
});

editors.register('jclass', class JClassEditor extends editors.Editor
{
	constructor(type = 'jclass')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);

		const subclasses = window.data.building_subclasses;
		this.input = dom(
			'select', null,
			subclasses.map(subclass => dom('option', { value: subclass },
				subclass.substr( subclass.lastIndexOf('.') + 1 )
			))
		);

		element.appendChild(this.input);
	}
	getValue()
	{
		return this.input.value;
	}
	setValue(val)
	{
		this.input.value = val;
	}
	defaultValue() { return 'ru.sdevteam.starlit.craft.buildings.Building'; }
});

editors.register('tech_level', class TechLevelEditor extends editors.Editor
{
	constructor(type = 'tech_level')
	{
		super(type);
	}
	attach(element)
	{
		super.attach(element);

		const levels = [0, 1, 2, 3];
		this.input = dom(
			'select', null,
			levels.map(lvl => dom('option', { value: lvl }, lvl + ''))
		);

		element.appendChild(this.input);
	}
	getValue()
	{
		return +this.input.value;
	}
	setValue(val)
	{
		this.input.value = val;
	}
	defaultValue() { return 0; }
});
