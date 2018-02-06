/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { APGroupMySuffixDialogComponent } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix-dialog.component';
import { APGroupMySuffixService } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.service';
import { APGroupMySuffix } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.model';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix';

describe('Component Tests', () => {

    describe('APGroupMySuffix Management Dialog Component', () => {
        let comp: APGroupMySuffixDialogComponent;
        let fixture: ComponentFixture<APGroupMySuffixDialogComponent>;
        let service: APGroupMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [APGroupMySuffixDialogComponent],
                providers: [
                    WlanGroupMySuffixService,
                    ZoneMySuffixService,
                    APGroupMySuffixService
                ]
            })
            .overrideTemplate(APGroupMySuffixDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(APGroupMySuffixDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(APGroupMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new APGroupMySuffix(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.aPGroup = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'aPGroupListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new APGroupMySuffix();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.aPGroup = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'aPGroupListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
