$(function ()
{
	$.getJSON('generated/data.json')
		.then(data =>
		{
			window.data = data;
			startup();
		})
	;

	window.container = $('#content .scroller');
	window.buildingRecords = [];
});

function startup()
{
	window.buildings = [];

	$('#reload_data').on('click', function (ev)
	{
		$('#file_open').click();
		ev.preventDefault();
		return false;
	});

	$('#save_data').on('click', function (ev)
	{
		let blob = new Blob([JSON.stringify(extractData())], { type: "application/json;charset=utf-8" });
		saveAs(blob, "buildings.json");
		ev.preventDefault();
		return false;
	});

	$('#reorder').on('click', function (ev)
	{
		console.log('Not implemented yet')
		ev.preventDefault();
		return false;
	});

	$('#restore_data').on('click', function (ev)
	{
		const saved = localStorage.getItem('autosave');
		if (saved && confirm('Are you sure to replace existing data?'))
		{
			renderBuildings(JSON.parse(saved));
		}
		else
		{
			alert('No autosaves available');
		}
		ev.preventDefault();
		return false;
	});

	$('#file_open').on('change', function (ev)
	{
		let file = this.files[0];
		console.log(file);
		if (file.type !== 'application/json')
		{
			alert('Wrong file type!');
			return;
		}
		if (file.name !== 'buildings.json' && !confirm('File is not named as buildings.json! Continue?'))
		{
			return;
		}

		const reader = new FileReader();
		reader.readAsText(file, "UTF-8");
		reader.onload = function (evt)
		{
			// window.buildings = ;
			renderBuildings(JSON.parse(evt.target.result));
		}
		reader.onerror = function (evt)
		{
			alert('Error while reading the file!');
		}
	});

	// autosave
	setInterval(function ()
	{
		if (window.buildingRecords.length)
			window.localStorage.setItem('autosave', JSON.stringify(extractData()));
	}, 1000 * 60);
}

function renderBuildings(buildings)
{
	window.container.html('');
	window.buildingRecords.length = 0;
	buildings.forEach(b =>
	{
		const br = new BuildingRecord(b);
		window.container.append(br.el);
		window.buildingRecords.push(br);
	});
}

function extractData()
{
	return window.buildingRecords.map(br =>
	{
		br.updateData();
		return br.data;
	});
}

function removeRecord(br)
{
	let i = window.buildingRecords.indexOf(br);
	if (i != -1)
	{
		window.buildingRecords.splice(i, 1);
		$(br.el).remove();
	}
}
function duplicateRecord(br)
{
	let i = window.buildingRecords.indexOf(br);
	if (i != -1)
	{
		br.updateData();
		const duplicated = new BuildingRecord(br.data);
		window.buildingRecords.splice(i, 0, duplicated);
		$(duplicated.el).insertAfter(br.el);
	}
}
function moveRecordUp(br)
{
	let i = window.buildingRecords.indexOf(br);
	if (i > 0)
	{
		$(br.el).remove();
		$(br.el).insertBefore(window.buildingRecords[i-1].el);
		[window.buildingRecords[i], window.buildingRecords[i-1]] = [
			window.buildingRecords[i-1], window.buildingRecords[i]
		];
	}
}
function moveRecordDown(br)
{
	let i = window.buildingRecords.indexOf(br);
	if (i < window.buildingRecords.length - 1 && i != -1)
	{
		$(br.el).remove();
		$(br.el).insertAfter(window.buildingRecords[i+1].el);
		[window.buildingRecords[i], window.buildingRecords[i+1]] = [
			window.buildingRecords[i+1], window.buildingRecords[i]
		];
	}
}
