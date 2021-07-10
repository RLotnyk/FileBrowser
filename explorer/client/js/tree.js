$(document).ready(function () {
    main();
});
function update() {
    $(document).ready(function(){
        setTimeout(
            function() {
                location.reload();
            }, 2000
        );
    });
}
function main() {
    $('#jstree')
        .jstree({
            "plugins": ['contextmenu', 'search', 'themes', 'html_data', 'icons', 'types'],

            'types': {
                "folder": {
                    "icon": "jstree-icon jstree-folder"
                },
                "file": {
                    "icon": "jstree-icon jstree-file"
                }
            },
                "search": {
                    "case_insensitive": false,
                    "ajax": {
                        "url": "search",
                        "type": "GET"
                    }
                },
                "core": {
                    "check_callback": true,
                    "data": {
                        "url": "tree",
                        "data": function (node) {
                            return {
                                "id": node.id
                            };
                        }
                    }
                },
                "contextmenu": {
                    "items": function ($node) {
                        var tree = $("#jstree").jstree(true);

                        return {
                            "Create": {
                                "separator_before": false,
                                "separator_after": false,
                                "label": "Create",
                                "action": function (obj) {
                                    var n = tree.create_node(tree.get_node($node));
                                    tree.edit(n, '', function (node, status) {
                                        if (node.text.length < 4 || !status) {
                                            console.log(node);
                                            this.delete_node(node);
                                            alert('Name must be at least 4 characters!')
                                        } else {
                                            $.ajax({
                                                url: 'create',
                                                type: 'POST',
                                                dataType: "json",
                                                contentType: "application/json",
                                                data: JSON.stringify({
                                                    id: $node.id,
                                                    text: node.text
                                                }),
                                            });
                                            update();
                                        }
                                    });
                                }
                            },
                            "Remove": {
                                "separator_before": false,
                                "separator_after": false,
                                "label": "Remove",
                                "action": function () {
                                    tree.delete_node($node);
                                    $.ajax({
                                        url: 'delete',
                                        type: 'POST',
                                        dataType:"json",
                                        contentType: "application/json",
                                        data: JSON.stringify({
                                            id: $node.id
                                        })
                                    });
                                    update();
                                }
                            }
                        }
                    }
                }
        });
    }