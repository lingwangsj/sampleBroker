import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { APMySuffix } from './ap-my-suffix.model';
import { APMySuffixService } from './ap-my-suffix.service';

@Component({
    selector: 'jhi-ap-my-suffix-detail',
    templateUrl: './ap-my-suffix-detail.component.html'
})
export class APMySuffixDetailComponent implements OnInit, OnDestroy {

    aP: APMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private aPService: APMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAPS();
    }

    load(id) {
        this.aPService.find(id).subscribe((aP) => {
            this.aP = aP;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAPS() {
        this.eventSubscriber = this.eventManager.subscribe(
            'aPListModification',
            (response) => this.load(this.aP.id)
        );
    }
}
