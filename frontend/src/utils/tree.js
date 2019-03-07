export function createTree(arry, labelName, valueName) {
  var options = []
  arry.forEach(element => {
    var value = element[valueName]
    var label = element[labelName]
    var children = element['children']
    var option = null
    if (children === undefined || children.length === 0) {
      option = { 'value': value, 'label': label }
    } else {
      option = { 'value': value, 'label': label, 'children': createTree(children, labelName, valueName) }
    }
    options.push(option)
  })
  return options
}
