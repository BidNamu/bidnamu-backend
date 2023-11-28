package com.bidnamu.bidnamubackend.item.domain;

import com.bidnamu.bidnamubackend.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category parent;

    @Column(nullable = false)
    private int depth = 0;

    @OneToMany(mappedBy = "parent")
    private final List<Category> children = new ArrayList<>();

    @Builder
    public Category(final String name, final Category parent) {
        this.name = name;
        updateParent(parent);
        this.depth = countDepth();
    }

    public void updateParent(final Category parent) {
        if (isCircularReference(parent)) {
            throw new IllegalArgumentException("카테고리 생성 시 순환참조가 감지되었습니다.");
        }
        this.parent = parent;
    }

    private boolean isCircularReference(final Category potentialParent) {
        Category current = potentialParent;
        while (current != null) {
            if (current == this) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    private int countDepth() {
        int count = 0;
        Category p = this.parent;
        while (p != null) {
            count++;
            p = p.getParent();
        }
        return count;
    }
}
