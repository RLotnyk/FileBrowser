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

            'sort' : function(a, b) {
                a1 = this.get_node(a);
                b1 = this.get_node(b);
                if (a1.icon == b1.icon){
                    return (a1.text > b1.text) ? 1 : -1;
                } else {
                    return (a1.icon > b1.icon) ? 1 : -1;
                }
            },
                "search": {
                    "case_sensitive": true
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
                                                data: {
                                                    id: $node.id,
                                                    name: node.text
                                                },
                                                success: function (data) {
                                                }
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
                                        data: {id: $node.id}
                                    });
                                    update();
                                }
                            }
                        }
                    }
                }
        });
    }