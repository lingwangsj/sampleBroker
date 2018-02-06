import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { APGroupMySuffix } from './ap-group-my-suffix.model';
import { APGroupMySuffixService } from './ap-group-my-suffix.service';

@Component({
    selector: 'jhi-ap-group-my-suffix-detail',
    templateUrl: './ap-group-my-suffix-detail.component.html'
})
export class APGroupMySuffixDetailComponent implements OnInit, OnDestroy {

    aPGroup: APGroupMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private aPGroupService: APGroupMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAPGroups();
    }

    load(id) {
        this.aPGroupService.find(id)
            .subscribe((aPGroupResponse: HttpResponse<APGroupMySuffix>) => {
                this.aPGroup = aPGroupResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAPGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'aPGroupListModification',
            (response) => this.load(this.aPGroup.id)
        );
    }
}
