package com.lotnyk.explorer.model;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Node {

    @NonNull
    @EqualsAndHashCode.Include
    private String id;

    private String parent;

    private String text;

    private String type;

}
